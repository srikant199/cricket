/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { CricketTestModule } from '../../../test.module';
import { PlayerDetailComponent } from '../../../../../../main/webapp/app/entities/player/player-detail.component';
import { PlayerService } from '../../../../../../main/webapp/app/entities/player/player.service';
import { Player } from '../../../../../../main/webapp/app/entities/player/player.model';

describe('Component Tests', () => {

    describe('Player Management Detail Component', () => {
        let comp: PlayerDetailComponent;
        let fixture: ComponentFixture<PlayerDetailComponent>;
        let service: PlayerService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CricketTestModule],
                declarations: [PlayerDetailComponent],
                providers: [
                    PlayerService
                ]
            })
            .overrideTemplate(PlayerDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PlayerDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlayerService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Player(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.player).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
