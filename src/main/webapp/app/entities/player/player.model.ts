import { BaseEntity } from './../../shared';

export class Player implements BaseEntity {
    constructor(
        public id?: number,
        public playername?: string,
        public playerNumber?: string,
        public playerPhoneNumber?: string,
        public playerAddress?: string,
        public teams?: BaseEntity[],
    ) {
    }
}
