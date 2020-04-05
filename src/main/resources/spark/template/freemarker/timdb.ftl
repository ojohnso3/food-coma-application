<#assign content>
<div style="text-align:center">
<font color = "white">
<h1> TIMDB TIME! </h1>

<p>
<form method="POST" action="/handoff">

    <style>
    
    body {background-image: url('https://cdn.bleacherreport.net/images_root/slides/photos/000/559/976/107525117_original.jpg?1292106728');
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    background-attachment: fixed;
    }
    
    </style>


<section>
<code>
<h3>
<p><strong><h2>Directions: Enter in the name of two actors to find the shortest path between them!</h2></strong></p><br>
<p><strong><h3>First Actor: </h3></strong></p>
<input type="text" id="source" name="source" value=""><br>
<p><strong><h3>Second Actor: </h3></strong></p>
<input type="text" id="dest" name="dest" value=""><br>
</h3>
</code>
</section>

<input type="submit">
</form>

</p>
<h3>
${hints}
</h3>
</font>

</div>

</#assign>
<#include "mainDb.ftl">
