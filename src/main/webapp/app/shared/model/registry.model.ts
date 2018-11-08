import { Moment } from 'moment';

export interface IRegistry {
    id?: number;
    firstName?: string;
    lastName?: string;
    dni?: string;
    birthdate?: Moment;
    phoneNumber?: string;
    business?: string;
    occupation?: string;
    email?: string;
    activationKey?: string;
    validationCode?: string;
    resetKey?: string;
    resetDate?: Moment;
    activated?: boolean;
    activationDate?: Moment;
    deleted?: boolean;
    appName?: string;
    appId?: number;
    subcategoryName?: string;
    subcategoryId?: number;
}

export class Registry implements IRegistry {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public dni?: string,
        public birthdate?: Moment,
        public phoneNumber?: string,
        public business?: string,
        public occupation?: string,
        public email?: string,
        public activationKey?: string,
        public validationCode?: string,
        public resetKey?: string,
        public resetDate?: Moment,
        public activated?: boolean,
        public activationDate?: Moment,
        public deleted?: boolean,
        public appName?: string,
        public appId?: number,
        public subcategoryName?: string,
        public subcategoryId?: number
    ) {
        this.activated = this.activated || false;
        this.deleted = this.deleted || false;
    }
}
