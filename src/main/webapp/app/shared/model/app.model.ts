export interface IApp {
    id?: number;
    name?: string;
    description?: string;
    version?: string;
    imageBlobContentType?: string;
    imageBlob?: any;
    image?: string;
    activated?: boolean;
    deleted?: boolean;
    subcategoryName?: string;
    subcategoryId?: number;
}

export class App implements IApp {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public version?: string,
        public imageBlobContentType?: string,
        public imageBlob?: any,
        public image?: string,
        public activated?: boolean,
        public deleted?: boolean,
        public subcategoryName?: string,
        public subcategoryId?: number
    ) {
        this.activated = this.activated || false;
        this.deleted = this.deleted || false;
    }
}
