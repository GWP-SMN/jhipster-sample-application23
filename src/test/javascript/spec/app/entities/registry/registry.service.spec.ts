/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { RegistryService } from 'app/entities/registry/registry.service';
import { IRegistry, Registry } from 'app/shared/model/registry.model';

describe('Service Tests', () => {
    describe('Registry Service', () => {
        let injector: TestBed;
        let service: RegistryService;
        let httpMock: HttpTestingController;
        let elemDefault: IRegistry;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(RegistryService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Registry(
                0,
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                currentDate,
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                currentDate,
                false,
                currentDate,
                false
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        birthdate: currentDate.format(DATE_FORMAT),
                        resetDate: currentDate.format(DATE_TIME_FORMAT),
                        activationDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Registry', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        birthdate: currentDate.format(DATE_FORMAT),
                        resetDate: currentDate.format(DATE_TIME_FORMAT),
                        activationDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        birthdate: currentDate,
                        resetDate: currentDate,
                        activationDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Registry(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Registry', async () => {
                const returnedFromService = Object.assign(
                    {
                        firstName: 'BBBBBB',
                        lastName: 'BBBBBB',
                        dni: 'BBBBBB',
                        birthdate: currentDate.format(DATE_FORMAT),
                        phoneNumber: 'BBBBBB',
                        business: 'BBBBBB',
                        occupation: 'BBBBBB',
                        email: 'BBBBBB',
                        activationKey: 'BBBBBB',
                        validationCode: 'BBBBBB',
                        resetKey: 'BBBBBB',
                        resetDate: currentDate.format(DATE_TIME_FORMAT),
                        activated: true,
                        activationDate: currentDate.format(DATE_TIME_FORMAT),
                        deleted: true
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        birthdate: currentDate,
                        resetDate: currentDate,
                        activationDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Registry', async () => {
                const returnedFromService = Object.assign(
                    {
                        firstName: 'BBBBBB',
                        lastName: 'BBBBBB',
                        dni: 'BBBBBB',
                        birthdate: currentDate.format(DATE_FORMAT),
                        phoneNumber: 'BBBBBB',
                        business: 'BBBBBB',
                        occupation: 'BBBBBB',
                        email: 'BBBBBB',
                        activationKey: 'BBBBBB',
                        validationCode: 'BBBBBB',
                        resetKey: 'BBBBBB',
                        resetDate: currentDate.format(DATE_TIME_FORMAT),
                        activated: true,
                        activationDate: currentDate.format(DATE_TIME_FORMAT),
                        deleted: true
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        birthdate: currentDate,
                        resetDate: currentDate,
                        activationDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Registry', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
