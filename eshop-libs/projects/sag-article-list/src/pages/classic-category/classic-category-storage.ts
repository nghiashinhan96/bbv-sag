export class ClassCategoryStorage {
    private selectedOilIds = [];
    set selectedOils(value: any) {
        const selectedIds: any = this.selectedOils;
        if (Array.isArray(value)) {
            (value || []).forEach(newVal => {
                const notFound = !selectedIds.find(val => val.key === newVal.key);
                if (notFound) {
                    selectedIds.push(newVal);
                }
            });
        } else {
            const notFound = !selectedIds.find(val => val.key === value.key);
            if (notFound) {
                selectedIds.push(value);
            }
        }
        this.selectedOilIds = [...selectedIds];
    }

    get selectedOils() {
        return this.selectedOilIds || [];
    }

    removeSelectedOil(key: string) {
        const selectedIds: any = this.selectedOils;
        const remaining = selectedIds.filter(val => val.key !== key);
        this.selectedOilIds = [...remaining];
    }

    removeAllSelectedOil() {
        this.selectedOilIds = [];
    }
}
