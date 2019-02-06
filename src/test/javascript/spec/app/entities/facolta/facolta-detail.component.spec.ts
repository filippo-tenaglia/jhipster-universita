/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UniversitaTestModule } from '../../../test.module';
import { FacoltaDetailComponent } from 'app/entities/facolta/facolta-detail.component';
import { Facolta } from 'app/shared/model/facolta.model';

describe('Component Tests', () => {
    describe('Facolta Management Detail Component', () => {
        let comp: FacoltaDetailComponent;
        let fixture: ComponentFixture<FacoltaDetailComponent>;
        const route = ({ data: of({ facolta: new Facolta(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UniversitaTestModule],
                declarations: [FacoltaDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FacoltaDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FacoltaDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.facolta).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
