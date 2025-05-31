$(function(){
  const $form = $('#searchForm');
  const $keyword = $('#keyword');
  
   /* ---------- 検索ボタン／Enter 送信時 ---------- */
  $form.on('submit', function(e){
    if ($keyword.val().trim() === '') {
      // 空検索は禁止
      e.preventDefault();
    }
  });
});