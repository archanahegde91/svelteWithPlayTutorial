

export class ApiError extends Error{
    public statusCode: number;
    public statusText: string;
    public details: string | undefined;

    constructor(statusCode:number, statusText:string, message:string, details?:string) {
        super(message);
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.details = details;
    }
}