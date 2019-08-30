export class ApiResponse{
    userid : string;
    message : string;
    constructor(userid, message){
        this.userid = userid;
        this.message = message;
    }
}