INSERT INTO `user` (
    user_id
) VALUES (
    '550e8400-e29b-41d4-a716-446655440000'
),
(
    '111e8400-e29b-41d4-a716-446655440111'
);


INSERT INTO product (
    product_id,
    product_name,
    price,
    product_description,
    stock,
    sale_status,
    created_at,
    updated_at
) VALUES(
    '97113c2c-719a-490c-9979-144d92905c33',
    'Item4',
    2900,
    'Item4の商品説明です。',
    10,
    '0',
    '2017-04-12 10:15:00',
    '2017-04-12 10:15:00'
),
(
    '09d5a43a-d24c-41c7-af2b-9fb7b0c9e049',
    'Item5',
    2500,
    'Item5の商品説明です。',
    5,
    '1',
    '2019-08-25 14:40:30',
    '2019-08-25 14:40:30'
),
(
    '6e1a12d8-71ab-43e6-b2fc-6ab0e5e813fd',
    'Item6',
    250,
    'Item6の商品説明です。',
    20,
    '1',
    '2021-11-03 09:05:12',
    '2021-11-03 09:05:12'
),
(
    '4a2a9e1e-4503-4cfa-ae03-3c1a5a4f2d07',
    'Item7',
    1800,
    'Item7の商品説明です。',
    0,
    '1',
    '2022-06-30 18:23:45',
    '2022-06-30 18:23:45'
),
(
    'f9c9cfb2-0893-4f1c-b508-f9e909ba5274',
    'Item18',
    3200,
    'Item18の商品説明です。',
    15,
    '1',
    '2023-09-10 11:11:11',
    '2023-09-10 11:11:11'
),
(
    '1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68',
    'Item19',
    750,
    'Item19の商品説明です。',
    20,
    '1',
    '2025-04-05 08:02:14',
    '2025-04-05 08:02:14'
);

insert into favorite (
    user_id,
    product_id
) values(
    '550e8400-e29b-41d4-a716-446655440000',
    '1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68'
);

INSERT INTO review (
    user_id,
    product_id,
    rating,
    comment,
    created_at,
    updated_at
) VALUES (
    '550e8400-e29b-41d4-a716-446655440000',     -- レビュアー
    '1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68',     -- Item19
    5,                                          -- ★5
    'コスパ抜群で毎回リピートしています！',       -- 500 文字以内
    '2025-05-25 12:10:30',                      -- 作成日時
    '2025-05-25 12:10:30'                       -- 更新日時
);
