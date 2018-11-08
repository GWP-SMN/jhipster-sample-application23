export interface ISubcategory {
    id?: number;
    name?: string;
    description?: string;
    deleted?: boolean;
    categoryName?: string;
    categoryId?: number;
}

export class Subcategory implements ISubcategory {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public deleted?: boolean,
        public categoryName?: string,
        public categoryId?: number
    ) {
        this.deleted = this.deleted || false;
    }
}
