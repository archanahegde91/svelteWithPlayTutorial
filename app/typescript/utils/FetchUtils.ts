import {ApiError} from "./ApiError";

export class FetchUtils {
    public static VALID_STATUS: number[] = [200, 201, 204];

    protected constructor() {
        //utility class
    }

    public static getCSRFObject(method: string, csrfToken: string): {} {
        return {
            method: method,
            headers: {'CSRF-TOKEN': csrfToken}
        };
    }


    public static getJsonDataObject(method: string, csrfToken: string, data: {} = {}): {} {
        let json = JSON.stringify(data);
        return {
            method: method,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'CSRF-TOKEN': csrfToken
            },
            body: json
        };
    }

    public static async post(url: string = '', csrfToken: string = '', data: {} = {}): Promise<Response> {
        let jsonDataObject = FetchUtils.getJsonDataObject("POST", csrfToken, data)
        let response = await fetch(url, jsonDataObject);
        return FetchUtils.validateResponseStatus(response);
    }

    public static async patch(url: string = '', csrfToken: string = '', data: {} = {}): Promise<Response> {
        let jsonDataObject = FetchUtils.getJsonDataObject("PATCH", csrfToken, data)
        let response = await fetch(url, jsonDataObject);
        return FetchUtils.validateResponseStatus(response);
    }

    public static async delete(url: string = '', csrfToken: string = ''): Promise<Response> {
        let csrfObject = FetchUtils.getCSRFObject("DELETE", csrfToken);
        let response = await fetch(url, csrfObject);
        return FetchUtils.validateResponseStatus(response);
    }

    public static async get(url: string): Promise<Response> {
        let response = await fetch(url);
        return FetchUtils.validateResponseStatus(response);
    }

    public static async fetchJson(url: string): Promise<JSON | void> {
        let response = await fetch(url);
        let validatedResponse = await FetchUtils.validateResponseStatus(response)
        return validatedResponse.json();
    }

    public static async validateResponseStatus(response: Response): Promise<Response> {

        if (!FetchUtils.VALID_STATUS.includes(response.status)) {
            const invalidStatus = "Invalid status: " + response.status + " " + response.statusText;
            let json;
            try {
                json = await response.json();
            } catch (error) {
                //the error page does not contain json data
            }
            let errorMessage;

            if (json == null) {
                errorMessage = invalidStatus;
            } else {
                errorMessage = json.message;
                if (json.requestId != null && json.showRequestId === true) {
                    errorMessage += " " + json.requestId;
                }
            }
            throw new ApiError(response.status, response.statusText,
                errorMessage,
                "Error while accessing api endpoint {" + response.url + "}. " + invalidStatus);
        }

        return response;
    }
}