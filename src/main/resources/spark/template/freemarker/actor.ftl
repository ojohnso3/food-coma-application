<#assign content>

    <style>
    
    body {background-image: url('https://2.bp.blogspot.com/-UQyAqqLrW8s/UWQpE0jWETI/AAAAAAAAS_g/rPCAQDVAKD0/s1920/popcorn-wallpapers.jpg');
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    background-attachment: fixed;
    }
    
    </style>

<header> <codeHead>
<div style="text-align:center">
  <p><strong><h3><font color = "blue">${welcoming}</font></h3></strong></p>
</div></codeHead></header>
<div style="text-align:center">
<section> <code>
<#list films as mov>

  <li><h3><a href="/film/${mov.encoded}">${mov.name}</a>
</h3></li>

</#list>
</code>
</section>
</div>

</#assign>

<#include "mainDb.ftl">