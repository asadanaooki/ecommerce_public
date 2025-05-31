$(function(){
  const $form = $('#searchForm');
  const $keyword = $('#keyword');
  const $sortSel = $('#sortSel');
  const $sortHidden = $('#sortHidden');
  
  const initKeyword = $keyword.prop('defaultValue');

  /* ---------- 並び替え変更時 ---------- */
  $sortSel.on('change',function(){
    $sortHidden.val(this.value);
    if (initKeyword.trim() === ''){
      $keyword.removeAttr('name');
    }
    $form.get(0).submit();
  });
});