export class StringUtil {
    static RemoveNonAlphaCharacter(input: string) {
        // Alphanumeric is a combination of alphabetic and numeric characters,
        // and is used to describe the collection of Latin letters and Arabic digits or a text constructed from the collection.
        // Alpha Characters :
        // Case Sensitive - (A-Z =26 )+ (a-z=26) =52
        // Case Insensitive - A-Z =26
        // Numeric Characters : 0-9 = 10
        return input.replace(/[^A-Za-z0-9 \* säüößéèàùâêîôûëïç]/g, '');
    }

    // remove non alpha character exclude hyphen (-) and dot(.) by regex
    static RemoveNonAlphaCharacterExcluded(input: string) {
        return input.replace(/[^[.]-A-Za-z0-9 \* säüößéèàùâêîôûëïç]/g, '');
    }
}