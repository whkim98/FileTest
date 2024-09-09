<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>파일테스트중</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {
            // 파일 수정 버튼 클릭 시
            $('.edit-btn').click(function() {
                var id = $(this).data('id');
                var prevFilename = $(this).data('filename'); // 현재 파일명 값 저장

                $('#file-form').attr('data-id', id);
                $('#previous_filename').val(prevFilename); // 이전 파일명을 hidden input에 저장
                $('#file-upload-modal').show();
            });

            // <td> 클릭 시 파일 다운로드
            $('.download-file').click(function() {
                var filename = $(this).data('filename');
                window.location.href = '/file/download?filename=' + filename; // 파일 다운로드 요청
            });

            // 파일 선택 이벤트 핸들러
            $('#myfile').change(function() {
                var filename = $(this).val().split('\\').pop();
                $('#filename').val(filename); // hidden input에 파일명 저장
            });

            // 폼 제출 이벤트 핸들러
            $('#file-form').submit(function(e) {
                e.preventDefault();

                var id = $(this).data('id');
                var formData = new FormData(this);
                formData.append('id', id);

                $.ajax({
                    url: '/file/updatee',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function(response) {
                        alert('파일이 업로드되었습니다.');
                        $('#file-upload-modal').hide();
                    },
                    error: function(xhr, status, error) {
                        alert('파일 업로드 실패');
                    }
                });
            });
        });
    </script>
    <style>
        #file-upload-modal {
            display: none;
            position: fixed;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            justify-content: center;
            align-items: center;
        }
        .modal-content {
            background: #fff;
            padding: 20px;
            border-radius: 8px;
        }
        .download-file {
            cursor: pointer;
            text-decoration: underline;
            color: blue;
        }
    </style>
</head>
<body>
<form action="/file/update" method="post" enctype="multipart/form-data">
    <table style="border: 1px solid">
        <tr>
            <th>파일 추가</th>
        </tr>
        <tr>
            <td><input type="file" name="myfile"></td>
        </tr>
        <tr>
            <td><button type="submit">보냄</button></td>
        </tr>
    </table>
</form>

<form id="file-form" action="/file/updatee" method="post" enctype="multipart/form-data" data-id="">
    <input type="hidden" name="filename" id="filename">
    <input type="hidden" name="previous_filename" id="previous_filename">

    <div id="file-upload-modal">
        <div class="modal-content">
            <h2>테스트중</h2>
            <input type="file" name="myfile" id="myfile" class="form-control" required="required">
            <button type="submit">보내기</button>
            <button type="button" onclick="$('#file-upload-modal').hide();">닫기</button>
        </div>
    </div>

    <table style="border: 1px solid">
        <c:forEach var="dto" items="${list}">
            <tr>
                <th>
                    <button type="button" class="edit-btn" data-id="${dto.id}" data-filename="${dto.table_name}">
                            ${dto.table_name}
                    </button>
                </th>
                <td class="download-file" data-filename="${dto.table_name}">
                        ${dto.table_name}
                </td>
                <td>
                    <button type="button" onclick="location.href='/file/delete?id=${dto.id}&&file_name=${dto.table_name}'">삭제</button>
                </td>
            </tr>
        </c:forEach>
    </table>
</form>
</body>
</html>
