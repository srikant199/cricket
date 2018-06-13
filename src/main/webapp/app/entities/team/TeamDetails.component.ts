import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {Team} from "./team.model";
import {Observable} from "rxjs/Observable";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {JhiEventManager} from "ng-jhipster";
import {TeamService} from "./team.service";
import {PlayerService} from "../player/player.service";
import {Player} from "../player/player.model";
@Component({
    selector: 'newTeams',
    templateUrl: './TeamDetails.html'
})
export class TeamDetailsComponent implements OnInit, OnDestroy {
    teamShow: boolean;
    playerShow:boolean;
    team: Team;
    isSaving: boolean;
    player: Player;
    private playerService: PlayerService;
    constructor(public activeModal:NgbActiveModal,private teamService:TeamService,private eventManager:JhiEventManager){
    this.teamShow =true;
    this.playerShow=true;
    }
    ngOnDestroy():void{ }
    ngOnInit():void{ }
    teamtuggle(){
        if (this.teamShow){
            this.teamShow = false;
        } else{
            this.teamShow = true;
        }}
        playertuggle(){
        if(this.playerShow){
            this.playerShow=false;
        }
        else{
            this.playerShow=true;
        }
    }
   /* private subscribeToSaveResponse(result: Observable<HttpResponse<Team>>){
        result.subscribe((res: HttpResponse<Team>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }
    private onSaveSuccess(result: Team){
       alert('succes');
    }
    private onSaveError(){
       alert('un success');
    }*/
    teamSave(){
        /*this.subscribeToSaveResponse(this.teamService.create(this.team));*/
    }
    PlayersDetails(){
       /* this.subscribeToSaveResponse(
            this.playerService.create(this.player));
*/
    }

}
