$(function(){
  const $form = $('#searchForm');
  const $keyword = $('#keyword');
  const $sortSel = $('#sortSel');
  const $sortHidden = $('#sortHidden');
  const $favBtn = $('button[data-product-id]');
  
  const initKeyword = $keyword.prop('defaultValue');
  
   /* ---------- 検索ボタン／Enter 送信時 ---------- */
  $form.on('submit', function(e){
    if ($keyword.val().trim() === '') {
      // 空検索は禁止
      e.preventDefault();
    }
  });

  /* ---------- 並び替え変更時 ---------- */
  $sortSel.on('change',function(){
    $sortHidden.val(this.value);
    if (initKeyword.trim() === ''){
      $keyword.removeAttr('name');
    }
    $form.get(0).submit();
  });
  
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
});