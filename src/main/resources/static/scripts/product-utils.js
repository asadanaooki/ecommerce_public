$(function(){
  const $favBtn = $('.fav-btn');
  const MIN   = 1;
  const MAX   = 20;
  const $qty  = $('#qtyInput');
  const $minus = $('#qtyMinus');
  const $plus  = $('#qtyPlus');
  const $msg   = $('#qtyLimitMsg');
  
  /* ----------  お気に入り ON/OFF ---------- */
  $favBtn.on('click',function(){
    $('#spinner').removeAttr('hidden');
    $favBtn.prop('disabled', true);
    
    const productId = $favBtn.data('product-id');
    const isOn = $favBtn.hasClass('is-active');
    const url = '/api/product/${productId}/favorite/' +
                   (isOn ? 'remove' : 'add');
    
    $.ajax({
      url: url,
      type: 'POST'
    })
    .done(function(){
      if (isOn){
        $favBtn.removeClass('is-active')
          .find('i')
            .removeClass('bi-heart-fill')
            .addClass('bi-heart');
      } else{
        $favBtn.removeClass('is-active')
          .find('i')
            .removeClass('bi-heart')
            .addClass('bi-heart-fill');
      }
    })
    .fail(function(){
      alert("error");
    })
    .always(function(){
      $favBtn.prop('disabled',false);
      $('#spinner').attr('hidden', true);
    })
  })
  
  /* ---------- 数量セレクター ---------- */
  function refreshQtyButtons () {
      const v = +$qty.val();
      $minus.prop('disabled', v <= MIN);
      $plus .prop('disabled', v >= MAX);
      $msg.toggleClass('d-none', v < MAX);
    }

    /* イベント */
    $minus.on('click', () => {
      const v = +$qty.val();
      if (v > MIN) {
        $qty.val(v - 1);
        refreshQtyButtons();
      }
    });

    $plus.on('click', () => {
      const v = +$qty.val();
      if (v < MAX) {
        $qty.val(v + 1);
        refreshQtyButtons();
      }
    });

    /* 初期化 */
    refreshQtyButtons();

});