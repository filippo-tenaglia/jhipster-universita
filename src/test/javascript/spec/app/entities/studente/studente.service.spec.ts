/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { StudenteService } from 'app/entities/studente/studente.service';
import { IStudente, Studente } from 'app/shared/model/studente.model';

describe('Service Tests', () => {
    describe('Studente Service', () => {
        let injector: TestBed;
        let service: StudenteService;
        let httpMock: HttpTestingController;
        let elemDefault: IStudente;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(StudenteService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Studente(0, currentDate, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        data_nascita: currentDate.format(DATE_FORMAT)
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

            it('should create a Studente', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        data_nascita: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        data_nascita: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Studente(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Studente', async () => {
                const returnedFromService = Object.assign(
                    {
                        data_nascita: currentDate.format(DATE_FORMAT),
                        nome: 'BBBBBB',
                        cognome: 'BBBBBB',
                        matricola: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        data_nascita: currentDate
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

            it('should return a list of Studente', async () => {
                const returnedFromService = Object.assign(
                    {
                        data_nascita: currentDate.format(DATE_FORMAT),
                        nome: 'BBBBBB',
                        cognome: 'BBBBBB',
                        matricola: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        data_nascita: currentDate
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

            it('should delete a Studente', async () => {
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
