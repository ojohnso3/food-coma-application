<#assign content>

<h1> Let's Look at Some Stars! </h1>



<p> by Matt Siff
<form method="GET" action="/radius">
<section>
  <code>
<p>Directions: Run the stars command to load a file or a neighbors or radius search to find the neighbors near<br> the coordinates or star you provide!</p>
<p><strong>Enter the command you would like to run below (i.e. stars, neighbors, radius):</strong></p>
 <input type="text" id="Type of Search" name="Type of Search" value=""><br>
 <p><strong>Enter the file path to load (for stars command):</strong></p>
<input type="text" id="FileToLoad" name="FileToLoad" value=""><br>
<p><strong>Enter the radius or number of neighbors below:</strong></p>
<p>What is the value you would like to enter? <br>For "radius" search: This is the radius to find stars within. <br>For the "neighbors" search: This is the number of neighbors you <br>would like to find.</p>
<input type="text" id="Value" name="Value" value=""><br>
<p><strong>Please enter search coordinates below:</strong></p>
<p>What is the X coordinate?</p>
<input type="text" id="X" name="X" value=""><br>
<p>What is the Y coordinate?</p>
<input type="text" id="Y" name="Y" value=""><br>
<p>What is the Z coordinate?</p>
<input type="text" id="Z" name="Z" value=""><br>
<p><strong>Please enter the name of a star below if <br>you did not enter search coordinates:</strong></p>
<p>Star's Name? (Note: Surround the name with quotes)</p>
<input type="text" id="Star's Name" name="Star's Name" value=""><br>
</code>
</section>



  <input type="submit">
</form>
${suggestions}
</p>


</#assign>
<#include "main.ftl">

