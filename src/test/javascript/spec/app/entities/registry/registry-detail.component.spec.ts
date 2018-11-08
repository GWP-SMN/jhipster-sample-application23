/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationWithGradleTestModule } from '../../../test.module';
import { RegistryDetailComponent } from 'app/entities/registry/registry-detail.component';
import { Registry } from 'app/shared/model/registry.model';

describe('Component Tests', () => {
    describe('Registry Management Detail Component', () => {
        let comp: RegistryDetailComponent;
        let fixture: ComponentFixture<RegistryDetailComponent>;
        const route = ({ data: of({ registry: new Registry(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterSampleApplicationWithGradleTestModule],
                declarations: [RegistryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RegistryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RegistryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.registry).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
