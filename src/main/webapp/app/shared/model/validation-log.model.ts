import { Moment } from 'moment';

export interface IValidationLog {
    id?: number;
    dateTime?: Moment;
    latitude?: number;
    longitude?: number;
    success?: boolean;
    deleted?: boolean;
    registryId?: number;
}

export class ValidationLog implements IValidationLog {
    constructor(
        public id?: number,
        public dateTime?: Moment,
        public latitude?: number,
        public longitude?: number,
        public success?: boolean,
        public deleted?: boolean,
        public registryId?: number
    ) {
        this.success = this.success || false;
        this.deleted = this.deleted || false;
    }
}
