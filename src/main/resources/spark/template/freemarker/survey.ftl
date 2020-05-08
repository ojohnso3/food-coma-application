<#assign content>

<h1>New User Survey!</h1>

<div class="skip">
    <p>If you don't want to fill out the survey, press below. <br>
    <em>NOTE</em>: this survey is for recipe recommendation purposes only, <br> and we 
    will maintain your privacy at all costs.</p>
    <input class="survey_button" id="survey_skip" type="submit" value="Skip the Survey Here">
    <br>
</div>
<br>
<br>
<div class="survey">
    <h3>Please check the nutrients you would like to prioritize:</h3>

    <div id="left_survey">
        <label class="check">Calcium
            <input id="calcium" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Carbs
            <input id="carbs" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Cholesterol
            <input id="cholesterol" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Energy
            <input id="energy" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Fat
            <input id="fat" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Fiber
            <input id="fiber" type="checkbox">
            <span class="checkmark"></span>
        </label>
        </label> 
        <label class="check">Iron
            <input id="iron" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Monounsaturated
            <input id="monounsaturated" type="checkbox">
            <span class="checkmark"></span>
        </label> 
        <label class="check">Polyunsaturated
            <input id="polyunsaturated" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Potassium
            <input id="potassium" type="checkbox">
            <span class="checkmark"></span>
        </label> 
        <label class="check">Protein
            <input id="protein" type="checkbox">
            <span class="checkmark"></span>
        </label> 
    </div>
    <div id="right_survey">
        <label class="check">Saturated
            <input id="saturated" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Sodium
            <input id="sodium" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Sugars
            <input id="sugars" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Sugars, added
            <input id="sugarsadded" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Trans
            <input id="trans" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Vitamin B12
            <input id="vitaminB12" type="checkbox">
            <span class="checkmark"></span>
        </label>
        </label> 
            <label class="check">Vitamin D
            <input id="vitaminD" type="checkbox">
            <span class="checkmark"></span>
        </label>
    </div>
    <br><br>
    <p>If you have any feedback or questions for the creators of foodCOMA,
    please write them below.</p>
    <input id="last_q" class="logbox"
        type="text"
        placeholder="Type here."
        value="">
    <br>
    <h2 id="survey_validity" aria-live=polite></h2>
</div>
<br>
<br>
<div class="submit">
    <input class="survey_button" id="survey_submit" type="submit" value="Submit the Survey Here">
    <br>
</div>

<p id="validity" aria-live=polite></p>

<script src="js/survey.js"></script>

</#assign>
<#include "main.ftl">
