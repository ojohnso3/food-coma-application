<#assign content>

<h1>New User Survey!</h1>

<img src="https://tinyurl.com/y86lsznl" id="foodCOMA"> </a>

<div class="skip">
    <p>If you don't want to fill out the survey, press the button below. <br>
    NOTE: this survey is for recipe recommendation purposes only, and we 
    will maintain your privacy at all costs.</p>
    <button id="survey_skip">Skip the Survey Here</button> 
    <br>
</div>
<br>
<br>
<div class="survey">
    <p>If you have any feedback or questions for the creators of foodCOMA,
    please write them below.</p>
    <input id="last_q" class="u-full-width"
        type="text"
        placeholder="Type here."
        value=""></input>
</div>
<br>
<br>
<div class="submit">
    <button id="survey_submit">Submit Survey Here!</button> 
    <br>
</div>


<p id="validity" aria-live=polite></p>



<script src="js/survey.js"></script>

</#assign>
<#include "main.ftl">