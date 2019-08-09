<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Webcodesk Letter</title>
    <style>
        @import url(https://fonts.googleapis.com/css?family=Open+Sans:300);
        body{
            font-family: 'Open Sans', sans-serif;
            background-color: #f5f5f5;
        }
        .grid {
            width: 100%;
            border: 0;
        }
        .header{
            text-align: center;
            vertical-align: middle;
        }
        .panel {
            border-radius: 5px;
            background-color: #fff;
            padding: 0.5em 2em 2em 2em;
        }

        .letter-body {
            padding: 1em 2em 2em 2em;
        }

        .header-img {
            width: 1em;
            margin-bottom: -0.1em;
        }

        .grid-side {
            width: 20%;
        }
        @media (max-width: 992px) {
            .grid-side {
                width: 5%;
            }
        }
        @media (max-width: 768px) {
            .grid-side {
                width: 0;
            }
            .letter-body {
                padding: 1em 0.5em 2em 0.5em;
            }
        }

        .footer {
            text-align: center;
        }
    </style>

</head>
<body >
<div class="letter-body">
    <table class="grid">
        <tbody>
        <tr>
            <td class="grid-side"></td>
            <td>
                <h1 class="header">
                    <span>Webcodesk</span>
                </h1>
            </td>
            <td class="grid-side"></td>
        </tr>
        <tr>
            <td class="grid-side"></td>
            <td>
                <div class="panel">
                    <h3>Dear subscriber!</h3>
                    <p>You were not asked to set a password for your account. It's time to do that.</p>
                    <p>Please click on the link below or copy it into your browser.</p>
                    <p style="text-align: center"><a href="${url}">${url}</a></p>
                    <p>Best regards,</p>
                    <p>Alex Pustovalov author of <a href="${serviceContextUrl}">Webcodesk</a></p>
                    <p style="border-top: 1px solid #f5f5f5; padding-top: 0.5em"><strong>P.S. </strong><small>If you didn't create account on Webcodesk site, just delete this letter.</small></p>
                </div>
            </td>
            <td class="grid-side"></td>
        </tr>
        <tr>
            <td class="grid-side"></td>
            <td>
                <p class="footer">
                    <small>Copyright © 2019 Alex Pustovalov</small>
                </p>
            </td>
            <td class="grid-side"></td>
        </tr>
        </tbody>
    </table>
</div>

</body>
</html>
