select
  /*%expand*/*
from
  SURVEY_OPTIONS
where
  GROUP_NAME = /* groupName */'survey'
order by
  ITEM_ORDER
