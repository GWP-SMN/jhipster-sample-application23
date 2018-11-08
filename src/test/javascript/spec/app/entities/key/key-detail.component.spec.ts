/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationWithGradleTestModule } from '../../../test.module';
import { KeyDetailComponent } from 'app/entities/key/key-detail.component';
import { Key } from 'app/shared/model/key.model';

describe('Component Tests', () => {
    describe('Key Management Detail Component', () => {
        let comp: KeyDetailComponent;
        let fixture: ComponentFixture<KeyDetailComponent>;
        const route = ({ data: of({ key: new Key(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterSampleApplicationWithGradleTestModule],
                declarations: [KeyDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(KeyDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(KeyDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.key).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
