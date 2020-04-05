
<#assign hints>
<p>

<header> <codeHead>
	<div style="text-align:center">
		<p><strong>The shortest path from those actors is:</strong></p>
	</div>
</codeHead></header>

<div style="text-align:center">
<#if VertDistPair??>
	<#list VertDistPair as pair>
		<li><a href="/actor/${pair.first.encoded}">${pair.first.name}</a> passes
		the golf ball to <a href="/actor/${pair.dest.encoded}">${pair.dest.name}</a>
		in the film <a href="/film/${pair.edge.movie.encoded}">${pair.edge.movie.name}</a>
		</li>
	</#list>
</#if>
<#if pairNoPath??>	
	<#list pairNoPath as pairNo>
		<li><a href="/actor/${pairNo.first.encoded}">${pairNo.first.name}</a> cannot pass
		the golf ball to <a href="/actor/${pairNo.dest.encoded}">${pairNo.dest.name}</a>
		because there is no valid path between ${pairNo.first.name} and ${pairNo.dest.name}!
		</li>
	</#list>
</#if>
</div>

</#assign>

<#include "timdb.ftl">