#!/bin/sh


# If this is a merge commit, then different rules apply. For now, skip commit message checks
if [ -f ".git/MERGE_MSG" ]; then
  echo "Currently merging, skipping commit message checks"
  exit 0
fi

# Enforce commit messages, based on http://addamhardy.com/2013/06/05/good-commit-messages-and-enforcing-them-with-git-hooks.html

echo "Verifying commit message format"

message_file=$1

function echoerr()
{
    echo "$@" 1>&2;
}

function is_comment()
{
    local line=$1

    local first_char=${line:0:1}
    if [ "$first_char" == "#" ]; then
        local check=0
    else
        local check=1
    fi
    return $check
}

function contains_ticket_id()
{
    local message=$1
    stdout=`echo "$message" | grep -E '[A-Z]+-[0-9]+'`
    # grep will return 0 if a match was found, non-zero otherwise
    return $?
}

function get_ticket_id()
{
    local message=$1
    stdout=`echo "$message" | grep -E '[A-Z]+-[0-9]+'`
    hasId=$?
    if [ $hasId -ne 0 ]; then
        return $hasId
    fi
    # Output the Ticket id to stdout for the caller to use
    # Cannot use 'grep -o <pattern>' as the '-o' flag doesn't work in cygwin
    # Cannot use '|' for OR regex matching, or '+' for {1, } occurence matching with 'sed' on Mac
    echo $stdout |
        sed -e 's/\(.*[^A-Z]* \)\([A-Z][A-Z]*\-[0-9][0-9]*\)/\2/g' | # Strip anything before the Ticket id
        sed -e 's/\([A-Z][A-Z]*\-[0-9][0-9]*\)\(.*\)/\1/g' |         # Strip anything after the Ticket id
        grep -E '^[A-Z]+-[0-9]+$'                                    # Check that what is left is just the Ticket id
    return $?
}

function check_format_rules()
{
    lineno=$1
    line=$2

    # Quoted lines start with '#' so skip those, but all others need to be 72 characters or less
    stdout=`is_comment $line`
    commented_line=$?
    if [ $commented_line != 0 ]; then
        trimmed_line=`echo "$line" | sed -e 's/^ *//' -e 's/ *$//'`
        line_length=${#line}
        if [ $lineno == 1 ]; then
            stdout=`contains_ticket_id "$line"`
            containsTicketId=$?

            minTitleLength=15
            maxFirstLineLength=50
            if [ $containsTicketId -eq 0 ]; then
                # Expect longer lines when a ticket id is present to ensure an adequate title is also present
                ticketId=`get_ticket_id $line`
                getTicketIdResult=$?
                if [ $getTicketIdResult -eq 0 ]; then
                    ticketIdLength=${#ticketId}
                    minTitleLength=$((minTitleLength + ticketIdLength + 1))
                else
                    echoerr "Error on line $lineno: Failed to retrieve ticket id from line"
                fi
            fi

            if [ $line_length -lt $minTitleLength ]; then
                echoerr "Error on line $lineno: First line must be at least $minTitleLength characters in" \
                        "length (including Ticket id, if present), but it is only $line_length characters long"
            fi
            if [ $line_length -gt $maxFirstLineLength ]; then
                echoerr "Error on line $lineno: First line should be no more than $maxFirstLineLength characters in" \
                        "length, but is $line_length characters long."
            fi
            if [ "$line" != "$trimmed_line" ]; then
                echoerr "Error on line $lineno: First line cannot contain leading or trailing whitespace"
            fi
        fi

        if [ $lineno == 2 ] && [ ! -z "$line" ]; then
            echoerr "Error on line $lineno: Second line should be empty. " \
                     "There needs to be a blank line between the summary (line 1) and the commit description"
        fi

        maxLineLength=72
        if [ $line_length -gt $maxLineLength ]; then
            echoerr "Error on line $lineno: Line is $line_length characters long - no line may exceed $maxLineLength " \
                    "characters"
        fi
    fi
}

continue=1

while [[ $continue != 0 ]]
do
    commit_msg=()
    errors=()
    line_no=0
message=`cat $message_file`

    stdout=`contains_ticket_id "$message"`
    containsTicketId=$?
    if [ $containsTicketId -ne 0 ]; then
        echo -e "\n########################################################################" \
                "\n# Commit rejected:                                                     #" \
                "\n#                                                                      #" \
                "\n# Commit message must include the ticket id of the issue that the      #" \
                "\n# change is for e.g:                                                   #" \
                "\n#                                                                      #" \
                "\n# FEATURE-379 List parts                                               #" \
                "\n# or                                                                   #" \
                "\n# BUG-480 Fix the bully critical bug                                   #" \
                "\n# or                                                                   #" \
                "\n# REFACTOR-738 Refactor the login code                                 #" \
                "\n#                                                                      #" \
                "\n#                                                                      #" \
                "\n########################################################################"

        exit 1
    fi

    old_ifs=$IFS
    IFS=
    while read -r line
    do
        stdout=`is_comment $line`
        commented_line=$?
        if [ $commented_line != 0 ]; then
            line_no=$((line_no+1))
        fi
        commit_msg+=("$line")

        temp_error_output="commit-line-errors.tmp"
        echo "" > $temp_error_output
        check_format_rules $line_no "$line" 2>$temp_error_output
        error_output=`cat "$temp_error_output"`
        while read -r error
        do
            # Add non empty lines from the error file as errors in the array
            if [ -n "$error" ]; then
                errors+=("$error")
            fi
        done <<< "$error_output"

        rm $temp_error_output
    done <<< "$message"
    IFS=$old_ifs

    if [ ${#errors[@]} -eq 0 ]; then
        echo "Commit message passes validation rules!"
        continue=false
        break
    else
        echo -e "\n####################################" \
                "\n# Commit rejected:                 #" \
                "\n# Incorrect commit message format  #" \
                "\n####################################"
        echo -e "\nErrors listed below:\n"
        # Write all of the validation error messages to the console
        for e in "${errors[@]}"; do
            echo "# $e"
        done

        # Write the original commit message to the console, with line numbers
        echo -e "\nProvided commit message (incl. line numbers):\n"
        line_no=0
        for l in "${commit_msg[@]}"; do
            stdout=`is_comment $l`
            commented_line=$?
            if [ $commented_line != 0 ]; then
                line_no=$((line_no+1))
                echo "$line_no: [$l]"
            fi
        done

        exit 1
    fi
done

# From Gerrit Code Review 2.12.2
#
# Part of Gerrit Code Review (https://www.gerritcodereview.com/)
#
# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
unset GREP_OPTIONS

CHANGE_ID_AFTER="Bug|Issue|Test"
MSG="$1"

# Check for, and add if missing, a unique Change-Id
#
add_ChangeId() {
    clean_message=`sed -e '
        /^diff --git .*/{
            s///
            q
        }
        /^Signed-off-by:/d
        /^#/d
    ' "$MSG" | git stripspace`
    if test -z "$clean_message"
    then
        return
    fi

    # Do not add Change-Id to temp commits
    if echo "$clean_message" | head -1 | grep -q '^\(fixup\|squash\)!'
    then
        return
    fi

    if test "false" = "`git config --bool --get gerrit.createChangeId`"
    then
        return
    fi

    # Does Change-Id: already exist? if so, exit (no change).
    if grep -i '^Change-Id:' "$MSG" >/dev/null
    then
        return
    fi

    id=`_gen_ChangeId`
    T="$MSG.tmp.$$"
    AWK=awk
    if [ -x /usr/xpg4/bin/awk ]; then
        # Solaris AWK is just too broken
        AWK=/usr/xpg4/bin/awk
    fi

    # Get core.commentChar from git config or use default symbol
    commentChar=`git config --get core.commentChar`
    commentChar=${commentChar:-#}

    # How this works:
    # - parse the commit message as (textLine+ blankLine*)*
    # - assume textLine+ to be a footer until proven otherwise
    # - exception: the first block is not footer (as it is the title)
    # - read textLine+ into a variable
    # - then count blankLines
    # - once the next textLine appears, print textLine+ blankLine* as these
    #   aren't footer
    # - in END, the last textLine+ block is available for footer parsing
    $AWK '
    BEGIN {
        # while we start with the assumption that textLine+
        # is a footer, the first block is not.
        isFooter = 0
        footerComment = 0
        blankLines = 0
    }

    # Skip lines starting with commentChar without any spaces before it.
    /^'"$commentChar"'/ { next }

    # Skip the line starting with the diff command and everything after it,
    # up to the end of the file, assuming it is only patch data.
    # If more than one line before the diff was empty, strip all but one.
    /^diff --git / {
        blankLines = 0
        while (getline) { }
        next
    }

    # Count blank lines outside footer comments
    /^$/ && (footerComment == 0) {
        blankLines++
        next
    }

    # Catch footer comment
    /^\[[a-zA-Z0-9-]+:/ && (isFooter == 1) {
        footerComment = 1
    }

    /]$/ && (footerComment == 1) {
        footerComment = 2
    }

    # We have a non-blank line after blank lines. Handle this.
    (blankLines > 0) {
        print lines
        for (i = 0; i < blankLines; i++) {
            print ""
        }

        lines = ""
        blankLines = 0
        isFooter = 1
        footerComment = 0
    }

    # Detect that the current block is not the footer
    (footerComment == 0) && (!/^\[?[a-zA-Z0-9-]+:/ || /^[a-zA-Z0-9-]+:\/\//) {
        isFooter = 0
    }

    {
        # We need this information about the current last comment line
        if (footerComment == 2) {
            footerComment = 0
        }
        if (lines != "") {
            lines = lines "\n";
        }
        lines = lines $0
    }

    # Footer handling:
    # If the last block is considered a footer, splice in the Change-Id at the
    # right place.
    # Look for the right place to inject Change-Id by considering
    # CHANGE_ID_AFTER. Keys listed in it (case insensitive) come first,
    # then Change-Id, then everything else (eg. Signed-off-by:).
    #
    # Otherwise just print the last block, a new line and the Change-Id as a
    # block of its own.
    END {
        unprinted = 1
        if (isFooter == 0) {
            print lines "\n"
            lines = ""
        }
        changeIdAfter = "^(" tolower("'"$CHANGE_ID_AFTER"'") "):"
        numlines = split(lines, footer, "\n")
        for (line = 1; line <= numlines; line++) {
            if (unprinted && match(tolower(footer[line]), changeIdAfter) != 1) {
                unprinted = 0
                print "Change-Id: I'"$id"'"
            }
            print footer[line]
        }
        if (unprinted) {
            print "Change-Id: I'"$id"'"
        }
    }' "$MSG" > "$T" && mv "$T" "$MSG" || rm -f "$T"
}
_gen_ChangeIdInput() {
    echo "tree `git write-tree`"
    if parent=`git rev-parse "HEAD^0" 2>/dev/null`
    then
        echo "parent $parent"
    fi
    echo "author `git var GIT_AUTHOR_IDENT`"
    echo "committer `git var GIT_COMMITTER_IDENT`"
    echo
    printf '%s' "$clean_message"
}
_gen_ChangeId() {
    _gen_ChangeIdInput |
    git hash-object -t commit --stdin
}


add_ChangeId

