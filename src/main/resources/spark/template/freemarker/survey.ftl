<#assign content>

<h1>New User Survey!</h1>

<img src="https://tinyurl.com/y86lsznl" id="foodCOMA">

<div class="skip">
    <p>If you don't want to fill out the survey, press the button below. <br>
    NOTE: this survey is for recipe recommendation purposes only, and we 
    will maintain your privacy at all costs.</p>
    <#--  <button id="survey_skip">Skip the Survey Here</button>   -->
    <input class="survey_button" id="survey_skip" type="submit" value="Skip the Survey Here">
    <br>
</div>
<br>
<br>
<div class="survey">
    <h3>Please input your dietary preferences below:</h3>

    <div id="left_survey">
        <label class="check">Calcium
            <input id="calcium" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Carbs
            <input id="carbs" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Cholestorol
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Monounsaturated
            <input type="checkbox">
            <span class="checkmark"></span>
        </label> 
            <label class="check">Polyunsaturated
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Sugars
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Fat
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Trans
            <input type="checkbox">
            <span class="checkmark"></span>
        </label> 
            <label class="check">Iron
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Fiber
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Folate (Equivalent)
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Potassium
            <input type="checkbox">
            <span class="checkmark"></span>
        </label> 
            <label class="check">Magnesium
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Sodium
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Vitamin B6
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
    </div>

    <div id="right_survey">
        <label class="check">Energy
            <input id="energy" type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Niacin (B3)
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Phosphorus
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Protein
            <input type="checkbox">
            <span class="checkmark"></span>
        </label> 
            <label class="check">Riboflavin (B2)
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Sugars, added
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Saturated
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Vitamin E
            <input type="checkbox">
            <span class="checkmark"></span>
        </label> 
            <label class="check">Vitamin A
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Vitamin B12
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Folate (food)
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Vitamin C
            <input type="checkbox">
            <span class="checkmark"></span>
        </label> 
            <label class="check">Vitamin D
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Vitamin K
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
        <label class="check">Thiamin (B1)
            <input type="checkbox">
            <span class="checkmark"></span>
        </label>
    </div>
    <br>
    <p>If you have any feedback or questions for the creators of foodCOMA,
    please write them below.</p>
    <input id="last_q" class="u-full-width"
        type="text"
        placeholder="Type here."
        value="">
    <br>
    <h2 id="survey_validity" aria-live=polite></h2>
</div>
<br>
<br>
<div class="submit">
    <#--  <button id="survey_submit">Submit Survey Here!</button>   -->
    <input class="survey_button" id="survey_submit" type="submit" value="Submit the Survey Here">
    <br>
</div>


<p id="validity" aria-live=polite></p>



<script src="js/survey.js"></script>

</#assign>
<#include "main.ftl">