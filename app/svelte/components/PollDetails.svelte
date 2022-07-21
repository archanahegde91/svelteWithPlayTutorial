<script lang="ts">
    import Card from '../shared/Card.svelte'
    import {createEventDispatcher} from 'svelte';
    import Button from '../shared/Button.svelte'
    import {tweened} from 'svelte/motion'
    import {FetchUtils} from "../../typescript/utils/FetchUtils";
    import {Poll} from "../../typescript/forms/Poll";

    export let deletePollCallBack;
    export let poll:Poll;
    export let votesAdded;

    const callCallback = (poll, option) => {
        votesAdded(poll, option);
    }


    const deleteCallCallBack = (uuid) =>{
        deletePollCallBack(uuid);
    }


    $: totalVotes = poll.votesa + poll.votesb
    $: percentA = Math.floor(100 / totalVotes * poll.votesa) || 0 ;
    $: percentB = Math.floor(100 / totalVotes * poll.votesb) || 0;

    //tweened percentages
    const tweenedA = tweened(0);
    const tweenedB = tweened(0);
    $: tweenedA.set(percentA)
    $: tweenedB.set(percentB)



    const dispatch = createEventDispatcher();
    let deleteSuccessful = false;

    async function postData(url = '') {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Accept': 'application/json, text/plain, */*'
            }
        });
        return response;
    }

    async function deleteData(url = '') {
        const response = await fetch(url, {
            method: 'DELETE'
        });
        return response;
    }


    const handleVote = (option, uuid) =>{
        let pollA = new Poll();
        if(poll.uuid == uuid){
            pollA.uuid = poll.uuid;
            pollA.question = poll.question;
            pollA.answerb = poll.answerb;
            pollA.answera = poll.answera;
            if(option == 'a'){
                pollA.votesa ++
            }
            if(option == 'b'){
                pollA.votesb ++
            }

            console.log("Handle" ,"vote")
            let csrfToken = (document.getElementsByName("csrfToken")[0] as HTMLInputElement).value;
            FetchUtils.post('/polls/vote',  csrfToken, pollA) .then(() => {
                console.log(pollA. votesa , pollA.votesb)
                callCallback(poll, option)
            })
                .catch(error => {
                    console.error("error" + error);
                });
        }

    }

    const handleDelete = () =>{
        deleteData("/polls/" + poll.uuid).then(function (response) {
            console.log(response);
            if (response.status != 200) {
                deleteSuccessful = false;
            } else {
                deleteSuccessful = true;
                deleteCallCallBack(poll.uuid)

            }
        }).catch(error => {
                console.log(error);
                deleteSuccessful = false;
            });

    }

</script>
<Card>
    <div class="poll">
        <h3>{poll.question}</h3>
        <p>Total votes : {totalVotes}</p>
        <div class="answer" on:click|preventDefault={() => handleVote('a', poll.uuid)}>
            <div class="percent percent-a" style="width:{$tweenedA}%"> </div>
            <span>{poll.answera} {poll.votesa}</span>

        </div>
        <div class="answer" on:click|preventDefault={() => handleVote('b', poll.uuid)}>
            <div class="percent percent-b"  style="width:{$tweenedB}%"> </div>
            <span>{poll.answerb} {poll.votesb}</span>
        </div>
        <div class="delete" on:click={() => handleDelete(poll.uuid)}>
            <Button flat=true>Delete</Button>
        </div>

    </div>
</Card>
<style>
    h3{
        margin: 0 auto;
        color: #555;
    }
    p{
        margin-top: 6px;
        font-size: 14px;
        color:#aaa;
        margin-bottom: 30px;
    }
    .answer{
        background:#fafafa;
        cursor:pointer;
        margin: 10px auto;
        position: relative;
    }
    .answer:hover{
        opacity: 0.6;
    }
    span{
        display: inline-block;
        padding: 10px 20px;
    }
    .percent{
        height: 100%;
        position: absolute;
        box-sizing: border-box;

    }
    .percent-a{
        background-color: rgba(217, 27, 66, 0.2);
        border-left:4px solid #d91b42;
    }
    .percent-b{
        background-color: rgba(27, 217, 66, 0.2);
        border-left:4px solid #45c496;
    }
    .delete{
        margin-top:30px;
        text-align: center;
    }
</style>