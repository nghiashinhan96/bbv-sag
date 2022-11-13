export class SearchTermUtil {
    public static getMakeModelTypeSearchTerm(value: string) {
        let result = value;
        
        try {
            const values = JSON.parse(value || '[]');
            result = values.length > 0 ? values.join(' ') : value;
        } catch { }

        return result;
    }

    public static getArticleFulltextSearchData(value : string) {
        let result = {
            mode: '',
            term: value,
            term_display: value,
            term_display_txt: value
        };
        
        try {
            const values = JSON.parse(value || '[]');
            if (values.length > 1) {
                result.mode = values.splice(0, 1);
                result.term = values[0];
                result.term_display = values.join(' &#x27F9; ');
                result.term_display_txt = values.join(' ⟹ ');
            }
        } catch { }

        return result;
    }
}