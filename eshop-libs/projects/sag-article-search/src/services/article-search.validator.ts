import { ValidatorFn, FormGroup, ValidationErrors } from '@angular/forms';

export const requireVehicleNameValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    const vehicleName = control.get('vehicleName');
    const vehicleYear = control.get('vehicleYear');
    return vehicleYear.value && !vehicleName.value ? { requiredVehicleName: true } : null;
};

export const yearValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    let isValid = false;
    if (!control.value || !control.value.trim()) {
        return null;
    }
    const pattern = [
        /^\d{4}$/, // JJJJ
        /^(0[1-9]|1[0-2])[-]\d{4}$/, // MM-JJJJ
        /^(0[1-9]|1[0-2])[/]\d{4}$/ // MM/JJJJ
    ];
    pattern.forEach(p => {
        isValid = isValid || p.test(control.value.trim());
    });
    return isValid ? null : { invalidYear: true };
};
