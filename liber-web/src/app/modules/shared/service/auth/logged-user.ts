export class LoggedUser {
    public userName: string
    public email: string
    public lang: string
    public roles: string[]
    public sytems: string[]

    constructor(
        userName: string,
        email: string,
        lang: string,
        roles: string[]
    ) {
        this.userName = userName
        this.email = email
        this.lang = lang
        this.roles = roles
        this.sytems = []
    }
}
