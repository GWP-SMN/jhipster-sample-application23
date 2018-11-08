export interface IKey {
    id?: number;
    code?: string;
    used?: boolean;
    deleted?: boolean;
    registryId?: number;
    appName?: string;
    appId?: number;
}

export class Key implements IKey {
    constructor(
        public id?: number,
        public code?: string,
        public used?: boolean,
        public deleted?: boolean,
        public registryId?: number,
        public appName?: string,
        public appId?: number
    ) {
        this.used = this.used || false;
        this.deleted = this.deleted || false;
    }
}
