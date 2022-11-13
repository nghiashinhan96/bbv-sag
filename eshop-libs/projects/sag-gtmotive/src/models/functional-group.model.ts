export class FunctionalGroup {
    functionalGroup: string;
    functionalGroupDescription: string;
    parts: Part[];
    open?: boolean;
}

export class Part {
    partCode: string;
    partDescription: string;
    functionalGroup?: FunctionalGroup;
    selected?: boolean;
}
