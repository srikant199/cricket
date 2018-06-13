import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CricketSharedModule } from '../../shared';
import {
    TeamService,
    TeamPopupService,
    TeamComponent,
    TeamDetailComponent,
    TeamDialogComponent,
    TeamPopupComponent,
    TeamDeletePopupComponent,
    TeamDeleteDialogComponent,
    teamRoute,
    teamPopupRoute,
} from './';
import {TeamDetailsComponent} from './team-details.component';

const ENTITY_STATES = [
    ...teamRoute,
    ...teamPopupRoute,
];

@NgModule({
    imports: [
        CricketSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TeamComponent,
        TeamDetailComponent,
        TeamDialogComponent,
        TeamDeleteDialogComponent,
        TeamPopupComponent,
        TeamDeletePopupComponent,
        TeamDetailsComponent,
    ],
    entryComponents: [
        TeamComponent,
        TeamDialogComponent,
        TeamPopupComponent,
        TeamDeleteDialogComponent,
        TeamDeletePopupComponent,
    ],
    providers: [
        TeamService,
        TeamPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CricketTeamModule {}
