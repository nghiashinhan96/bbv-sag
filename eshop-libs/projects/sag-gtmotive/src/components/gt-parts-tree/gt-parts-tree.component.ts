import { Component, Input, OnInit, Output, EventEmitter, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { keys } from 'lodash';
import { FunctionalGroup } from '../../models/functional-group.model';

@Component({
    selector: 'sag-gtmotive-parts-tree',
    templateUrl: './gt-parts-tree.component.html'
})
export class SagGtmotivePartsTreeComponent implements OnInit, OnDestroy {
    @Input() partsTree: FunctionalGroup[];
    @Output() pickPartOnTree = new EventEmitter();

    separatedColumn: any[];

    searchText: string;
    searchErrorMessage: string;

    private searchTextChange = new Subject<string>();

    constructor() { }

    ngOnInit() {
        this.separateTreeCols(this.partsTree);
        this.searchTextChange
            .pipe(
                debounceTime(300),
                distinctUntilChanged()
            )
            .subscribe(value => this.searchInPartTree(value));
    }

    ngOnDestroy() {
        this.searchTextChange.complete();
    }

    onSearchTextChange(event) {
        this.searchTextChange.next(event.target.value);
    }

    handlePickPartOnTree(zoneId, partCode) {
        this.pickPartOnTree.emit({ zoneId, partCode });
    }

    setCollapseState(funcGroup) {
        funcGroup.open = !funcGroup.open;
    }

    private separateTreeCols(source: FunctionalGroup[]) {
        this.separatedColumn = [];
        const colItemsNumber = Math.round(source.length / 3);
        let index = 0;
        while (index <= source.length) {
            if (index === colItemsNumber * 2) {
                this.separatedColumn.push(source.slice(index, source.length));
                break; // last col
            } else {
                this.separatedColumn.push(source.slice(index, index + colItemsNumber));
            }
            index += colItemsNumber;
        }
    }

    private searchInPartTree(textSearch) {
        this.searchErrorMessage = null;

        if (!(textSearch || '').trim()) {
            this.separateTreeCols(this.partsTree);
            return;
        }

        const results = [];

        for (const item of this.partsTree) {
            const filteredParts = item.parts.filter(p => this.hasTextInObject(p, textSearch));

            if (this.hasTextInObject(item, textSearch) || filteredParts.length) {
                const group = this.highlighTextInObject(item, 'functionalGroupDescription', textSearch);
                group.open = false;

                if (filteredParts.length) {
                    group.parts = filteredParts;
                    group.open = true;
                }

                group.parts = group.parts.map(p => this.highlighTextInObject(p, 'partDescription', textSearch));

                results.push(group);
            }
        }

        if (results.length) {
            this.separateTreeCols(results);
        } else {
            this.separatedColumn = [];
            this.searchErrorMessage = 'SEARCH.NO_RESULTS_FOUND';
        }
    }

    private hasTextInObject(source, searchText) {
        for (const key of keys(source)) {
            const value = source[key];

            if (`${value}`.search(new RegExp(searchText, 'gi')) !== -1) {
                return true;
            }
        }
        return false;
    }

    private highlighTextInObject(source, property, searchText) {
        const obj = { ...source };

        const value = obj[property];

        obj[property] = `${value}`.replace(new RegExp(searchText, 'gi'), (str) => `<mark>${str}</mark>`);

        return obj;
    }
}
