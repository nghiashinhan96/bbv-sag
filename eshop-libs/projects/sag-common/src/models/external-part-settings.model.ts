import { SagEditorLanguageModel } from "./sag-editor-language.model";

export class ExternalPartSettings {
    useExternalParts: boolean;
    showInReferenceGroup: boolean;
    headerNames: SagEditorLanguageModel[];
    orderTexts: SagEditorLanguageModel[];
}