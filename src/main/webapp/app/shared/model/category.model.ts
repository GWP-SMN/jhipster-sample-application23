export interface ICategory {
    id?: number;
    name?: string;
    description?: string;
    deleted?: boolean;
}

export class Category implements ICategory {
    constructor(public id?: number, public name?: string, public description?: string, public deleted?: boolean) {
        this.deleted = this.deleted || false;
    }
}
