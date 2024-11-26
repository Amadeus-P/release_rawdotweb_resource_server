
    // 댓글 글자수 세기
    function calc(){
        document.getElementById('text-count').innerText=
            document.getElementById('comment').value.length;
    }
    
        // 마우스 클릭시 url 앞에 https:// 넣어줌
        const input = document.getElementById('url-prefix');
        input.addEventListener('click', function() {
        if (!this.value.startsWith('https://')) {
        this.value = 'https://';
    }
    });
        // focus시 url 앞에 https:// 넣어줌
        input.addEventListener('focus', function() {
        if (!this.value.startsWith('https://')) {
        this.value = 'https://';
    }
    });
    