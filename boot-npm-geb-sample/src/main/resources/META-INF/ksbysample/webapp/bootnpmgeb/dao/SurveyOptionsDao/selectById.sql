select
  /*%expand*/*
from
  SURVEY_OPTIONS
where
  GROUP_NAME = /* groupName */'a'
  and
  ITEM_VALUE = /* itemValue */'a'
