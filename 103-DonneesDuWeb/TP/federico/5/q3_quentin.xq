(:
let $t :=
<a>
  <c>1</c>

  <d>2</d>
  <c>4</c>
  <d>3</d>
</a>

return $t//d/preceding-sibling::c

return $t//c[following-sibling::d]

:)

(:
let $t :=
<ro>
   <c>

    <a>1</a>
    <a>2</a>
  </c>

  <e>1</e>
  <c>
    <e>2</e>
    <a>1</a>
    <a>2</a>
  </c>
  <e>3</e>
  <a>3</a>

  <c>
      <e>4</e>
      <a>4</a>
  </c>
</ro>

return $t//c/a/preceding-sibling::a/preceding::e
A gauche: cas où e et les deux a sont tous fils de c, à droit le cas où le c commence après le e
return ($t//c/e[following-sibling::a[following-sibling::a]]) | ($t//e[following::c/a/following-sibling::a])
:)

(:
let $t :=
<ro>
  <b>
    <d>1</d>
    <c></c>
  </b>

   <b>

    <c></c>
    <d>2</d>
  </b>

   <b>

    <c> <d>3</d></c>

  </b>
</ro>

return $t//d[parent::b/c]

return $t//b[./c]/d
:)

(:
let $t:=
<ro>
  <r>
    <b>1</b>
    <c>1</c>
    <d>1
      <b>3</b>
    </d>
    <e>
      <f></f>
    </e>
    <g></g>
    <d>2</d>  this <d></d> should not be selected because the <e>2</e> has no child
    <e>2</e>
  </r>
  <b>2</b>
</ro>

return $t/r/b/..//*/./../preceding::d
Au lieu de regarder descendant-or-self etremonter au parent on regarde les descendant-or-self qui ont un enfant. Pas besoin de gérer le parent de r puisque c'est la racine
return $t/r[./b]//*[child::*]/preceding::d
:)

(:
let $t:=
<ro>
<a>
  <c>
    <f>
      <a>1</a>
    </f>
  </c>
</a>

</ro>

return $t//a/ancestor::c/child::d/parent::e

return ()

Un tel arbre XML ne peut pas exister.
:)
(:
let $t:=
<ro>
  <c>1</c>
  <b>
    <c>2</c>
    <f>
      <e>
        <c>3</c>
      </e>
    </f>
  </b>


    <c>4</c>
  <b>
    <c>5</c>
    <d>
      <e>
        <c>6</c>
      </e>
    </d>
  </b>
  <d>matcher</d>

  <c>7</c>
  <b>
    <c>8</c>
  </b>

</ro>

return $t//c[preceding::d]

return $t//d/following::c
:)

(:
let $t:=
<ro>
  <b>1</b>
  <b>2
    <a>
      <b>3</b>
    </a>
  </b>

  <b>4</b>
  <c>
    <b>5</b>
  </c>
</ro>

return $t//a/following::b

return $t//a//ancestor::*/following-sibling::*/descendant-or-self::b
:)
(:
let $t:=
<ro>
  <b>2</b>
  <d>
    <b>3</b>
  </d>
  <d>
    <a>1</a>
  </d>
  <b>1
    <a></a>
  </b>

</ro>

return $t//a/following::b

return $t//a//ancestor::*/following-sibling::*/descendant-or-self::b


return $t//a/preceding::b

return $t//a//ancestor::*/preceding-sibling::*/descendant-or-self::b
:)

(:5 Requête 5 impossible le d doit être enfant d'un c et avoir pour parent un e :)
(: Solution: une autre requête impossible puisqu'elle renverra bien la même chose (c'est-à-dire rien:)
