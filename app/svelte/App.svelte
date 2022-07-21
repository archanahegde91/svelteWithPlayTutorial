<script lang="ts">
    import Header from './components/Header.svelte'
    import Footer from "./components/Footer.svelte";
    import Tabs from "./shared/Tabs.svelte"
    import CreatePollForm from "./components/CreatePollForm.svelte";
    import PollList from "./components/PollList.svelte";
    import {Poll} from "../typescript/forms/Poll";

    let items = ["Current Polls", 'Add New Poll']
    let activeItem = 'Current Polls';
    let polls: Poll[] =[]
    let errorFetch = false;

    const tabChange = (e) => {
        activeItem = e.detail;
    }

    const handleAdd = (e) => {
        activeItem = 'Current Polls'
    };


    fetch('/polls')
    .then((resp) => resp.json())
    .then(function(data){
        console.log(data)
        polls = data
        errorFetch = false;
    }).catch(function (error){
        errorFetch = true;
    });

    const newPollAdded = (newPoll) =>{
        polls = [newPoll, ...polls]
        console.log("Poll Added " + newPoll)
    }

    const addVotes = (poll, option) =>{
        let index = polls.findIndex((p => p.uuid == poll.uuid))

        if(option =='a'){
            console.log("option added" + option)
            polls[index].votesa ++
        }

        if(option =='b'){
            polls[index].votesb ++
        }
//todo not working properly
        polls = polls
    }

    const deletePollUpdate = (uuid) =>{
        let deletededPolls = polls.filter(p => p.uuid != uuid);
        console.log("in delete call back " + deletededPolls.length)
        polls = [...deletededPolls];
    }

</script>
<Header/>
{#if errorFetch}
    <p class="warn">Polls konnten nicht geladen werden!</p>
{/if}
<main>
    <Tabs {activeItem} {items} on:tabChange={tabChange}/>
    {#if activeItem === 'Current Polls'}
        {#if polls.length < 1}
            <div class="info"> There are currently no active Polls</div>
            {/if}
        <PollList polls="{polls}" votesAdded={addVotes} deletePollCallBack ={deletePollUpdate}/>
    {:else if activeItem === 'Add New Poll'}
       <form>
           <CreatePollForm  pollAddedCallback={newPollAdded} on:add={handleAdd}/>
       </form>
    {/if}
</main>
<Footer/>
<style>
    main{
        max-width:960px;
        margin: 40px auto;
    }
    .info{
        text-align: center;
        color:#d91b42;
        font-size: 16px;
    }
</style>