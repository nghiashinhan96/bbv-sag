import { FormGroup } from '@angular/forms';

export class FormUtil {
    public static markFormAsDirty(form: FormGroup): void {
        if (!form) {
            return;
        }

        for (const key in form.controls) {
            if (form.controls.hasOwnProperty(key)) {
                const control = form.controls[key];
                control.markAsDirty();
                control.markAsTouched();
                control.updateValueAndValidity();
            }
        }
    }

    public static buildFormData(json, actionLink) {
        const f = document.createElement('form');
        f.setAttribute('method', 'post');
        f.setAttribute('action', actionLink);
        f.setAttribute('target', '_blank');
        Object.keys(json).forEach(k => {
            const i = document.createElement('input');
            const key = `__authentication[Neos][Flow][Security][Authentication][Token][${k.toLowerCase()}]`;
            const val = json[k];
            i.setAttribute('type', 'hidden');
            i.setAttribute('name', key);
            i.setAttribute('value', val);
            f.appendChild(i);
        });
        const s = document.createElement('input');
        s.setAttribute('type', 'submit');
        s.setAttribute('value', 'Submit');
        f.appendChild(s);
        return f;
    }
}
