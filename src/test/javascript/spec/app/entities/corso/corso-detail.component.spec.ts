/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UniversitaTestModule } from '../../../test.module';
import { CorsoDetailComponent } from 'app/entities/corso/corso-detail.component';
import { Corso } from 'app/shared/model/corso.model';

describe('Component Tests', () => {
    describe('Corso Management Detail Component', () => {
        let comp: CorsoDetailComponent;
        let fixture: ComponentFixture<CorsoDetailComponent>;
        const route = ({ data: of({ corso: new Corso(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UniversitaTestModule],
                declarations: [CorsoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CorsoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CorsoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.corso).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
