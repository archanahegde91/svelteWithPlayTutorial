<script>
    import {fade, slide, scale} from 'svelte/transition';
    import {flip} from 'svelte/animate';
    import PollDetails from './PollDetails.svelte';
    export let polls;
    export let votesAdded;
    export let deletePollCallBack;
</script>

<div class="poll-list">
    {#each polls as poll (poll.uuid)}
        <div in:fade out:scale|local animate:flip={{duration:500}} >
            <PollDetails poll={poll} deletePollCallBack = {deletePollCallBack} votesAdded={votesAdded}/>
        </div>
    {/each}
</div>


<style>
    .poll-list{
        display: grid;
        grid-template-columns: 1fr 1fr;
        grid-gap: 20px;

    }
</style>