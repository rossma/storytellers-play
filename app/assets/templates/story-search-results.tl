 <div id="results-blk" >

  <!-- Button trigger modal -->

  <div class="wrapper">
    <div class="masonry-cols">
      {#pages}
      <div class="pin">
        <a data-toggle="modal" href="#pageDetailModal" class="btn btn-default" data-title="{title}"
          data-img="{image}" data-series-id="{series.id}" data-series-name="{series.name}"> 
          <img src="{image}" />
        </a>
        <p>{summary}</p>
      </div>
      {/pages}
    </div>
  </div>
</div>
