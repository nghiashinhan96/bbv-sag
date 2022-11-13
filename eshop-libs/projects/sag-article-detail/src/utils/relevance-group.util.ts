import { RELEVANCE_GROUP } from "../enums/relevance-group.enum";

export class RelevanceGroupUtil {
    static getRelevanceGroupOrder(relevanceGroupType) {
        switch (relevanceGroupType) {
            case RELEVANCE_GROUP.DIRECT_MATCH:
                return 1;
            case RELEVANCE_GROUP.ORIGINAL_PART:
                return 2;
            case RELEVANCE_GROUP.REFERENCE_MATCH:
                return 3;
            case RELEVANCE_GROUP.POSSIBLE_MATCH:
                return 4;
            default:
                return 0;
        }
    }
}