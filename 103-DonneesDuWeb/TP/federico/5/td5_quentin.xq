(:
declare function local:strongEq($n1,$n2) as xs:boolean {
  let $ret :=
    if (not($n1) or not($n2))
    then (not($n1) and not($n2))
  else
  
  if ($n1 instance of element())
  then if ( not($n2 instance of element()))
          then local:strongEqEl($n1,$n2)
    
  else
  
    (($n1[1]=$n2[2]) and local:strongEq($n1[position() = (2 to last())],$n2[position() = (2 to last())]))
  return $ret  
  
};

declare function local:strongEqEl($n1,$n2) as xs:boolean {
  let $a:= 3
  return true
};

declare function local:eq($n1,$n2) {
    if (count($n1)!= count($n2))
      then false
    else
        if (count($n1)=0) then true
        else
          if (count($n1) > 1)
            then (local:eq($n1[1],$n2[1]) and
              local:eq($n1[position() >1],
                       $n2[position() >1]))
          else 
            if ($n1 instance of element())
              then  if (not($n2 instance of element()))
                          then false
                    else
                      ((name($n1) = name($n2)) and
                        local:eq($n1/child::*,$n2/child::*))
                   else
                    ($n1 = $n2)  
};


:)

declare function local:eq($n1,$n2) {
    if (count($n1)!= count($n2))
      then false()
    else
        if (count($n1)=0) then true()
        else
          if (count($n1) > 1)
            then (local:eq($n1[1],$n2[1]) and
              local:eq($n1[position() >1],
                       $n2[position() >1]))
          else
            if (not($n1 instance of element()))
              then ($n1 = $n2)
            else
              if (not($n2 instance of element())) then false()
              else ((name($n1) = name($n2)) and ($n1/text() = $n2/text()) and
                        local:eq(($n1/child::*),($n2/child::*)))
           
  
};

let $a:= <tree>manger</tree>
let $b:= "manger"
let $c:= ("manger","st")
let $d:= <notTree>manger</notTree>
let $e:= <tree>
          <branch>Tue</branch>
          <branch>sday</branch>
          </tree>
let $f:= <tree>
          <branch>Tuesday</branch>
          <branch>y</branch>
          </tree>          
let $g:= <branch>hello</branch>
let $h:= <branch>hell</branch>
return local:eq($e,$f)