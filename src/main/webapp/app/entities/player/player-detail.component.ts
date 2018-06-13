import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Player } from './player.model';
import { PlayerService } from './player.service';

@Component({
    selector: 'jhi-player-detail',
    templateUrl: './player-detail.component.html'
})
export class PlayerDetailComponent implements OnInit, OnDestroy {

    player: Player;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private playerService: PlayerService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPlayers();
    }

    load(id) {
        this.playerService.find(id)
            .subscribe((playerResponse: HttpResponse<Player>) => {
                this.player = playerResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPlayers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'playerListModification',
            (response) => this.load(this.player.id)
        );
    }
}
