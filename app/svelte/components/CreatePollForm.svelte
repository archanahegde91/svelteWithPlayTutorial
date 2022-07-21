<script lang="ts">
    import Button from '../shared/Button.svelte'
    import {createEventDispatcher} from 'svelte';
    import {Poll} from "../../typescript/forms/Poll";
    import {FetchUtils} from "../../typescript/utils/FetchUtils";

    let dispatch = createEventDispatcher();
    let fields = {question:'', answera:'', answerb:''}
    let errors =  {question:'', answera:'', answerb:''}
    let valid = true;

    declare const createRoutes: any;
    export let pollAddedCallback;

    const callCallback = (newPoll) => {
        pollAddedCallback(newPoll);
    }

    const submitHandler = () => {
        let isQuestionValid = false;
        let isAnsAValid = false;
        let isAnsBValid = false;
        if(fields.question.trim().length < 5){
            valid =false;
            errors.question = 'Questions must be atleast 5 charecters long';
        }else{
            isQuestionValid =true;
            errors.question= '';
        }

        if(fields.answera.trim().length < 1){
            valid =false;
            errors.answera = 'Answer cannot be empty';
        }else{
            isAnsAValid = true;
            errors.answera= '';
        }

        if(fields.answerb.trim().length < 1){
            valid = false;
            errors.answerb = 'Answer cannot be empty';
        }else{
            isAnsBValid = true;
            errors.answerb= '';
        }
        if(isQuestionValid && isAnsAValid && isAnsBValid){
            valid = true;
        }

        if(valid){
            let poll = {...fields, votesa: 0, votesb: 0, uuid: Math.random()}
            console.log(poll + "poll")
            let pollA = new Poll();
            pollA.question = poll.question;
            pollA.answera = poll.answera;
            pollA.answerb= poll.answerb;
            pollA.votesa= poll.votesa;
            pollA.votesb = poll.votesb;

            let url = "/polls/create";
            let csrfToken = (document.getElementsByName("csrfToken")[0] as HTMLInputElement).value;

            FetchUtils.post(url, csrfToken, pollA)
                .then(() => {
                    callCallback(pollA);
                    dispatch('add');
                })
                .catch(error => {
                    console.error("error" +error);
                });

        }else{
            console.log("Invalid")
        }
    }
</script>
<form on:submit|preventDefault={submitHandler}>
    <div class="form-field">
        <label for="question">Poll Question:</label>
        <input type="text" id="question" bind:value={fields.question}>
        <div class="error">{errors.question}</div>
    </div>
    <div class="form-field">
        <label for="answer-a">Answer A:</label>
        <input type="text" id="answer-a" bind:value={fields.answera} >
        <div class="error">{errors.answerb}</div>
    </div>
    <div class="form-field">
        <label for="answer-b">Answer B:</label>
        <input type="text" id="answer-b" bind:value={fields.answerb}>
        <div class="error">{errors.answerb}</div>
    </div>
    <Button type="secondary" inverse={false}>Add Poll</Button>
</form>

<style>
    form{
        width: 400px;
        margin:0 auto;
        text-align: center;
    }
    .form-field{
        margin: 18px;
    }
    input{
        width: 100%;
        border-radius: 6px;
    }
    label{
        margin: 10px auto;
        text-align: left;
    }
    .error{
        font-weight: bold;
        font-size: 12px;
        color: #d91b42;

    }
</style>