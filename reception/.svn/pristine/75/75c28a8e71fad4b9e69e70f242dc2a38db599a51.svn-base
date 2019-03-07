; ---------------------
;      Java.nsh
;      Written by: Johan Melin
;      Fonctions et page de detection de java
; ---------------------

;--------------------------------
; Constantes
;--------------------------------

; URL de telechargement de Java
!define JavaDownloadUrl "http://java.com/fr/download/manual.jsp"
; Titre de la page Java
!define JavaPageTitle "Verification de la presence et de la version de Java"
; Sous titre de la page Java
!define JavaPageSubTitle "Le programme d'installation verifie si java est bien installe sur votre ordinateur et que la version est assez recente."
  
;--------------------------------
; Variables
;--------------------------------

; Architecture processeur (32 ou 64 bits)
Var ArchiProc
; Version de java presente sur la machine
Var JavaCurrentVersion
; Version de java requise
Var JavaRequiredVersion
   
; Variable pour stocker le texte d'un libelle
Var JavaTextVersion
; Seconde variable pour stocker le texte d'un libelle
Var JavaTextVersion2
  
;--------------------------------
; Variables des widgets de la fenetre
;--------------------------------

; Boite de dialogue
Var JavaDialog
; Libelle de l'architecture du processeur
Var LabelArchiProc
; Libelle de la version de Java presente
Var LabelJavaCurrentVersion
; Libelle de la version de Java requise
Var LabelJavaRequiredVersion
; Libelle java text version
Var LabelJavaTextVersion
; Second libelle java text version
Var LabelJavaTextVersion2
; Libelle de l'url de telechargement de java
Var LabelJavaDownloadURL
  
;--------------------------------
; Detection de l'architecture processeur (32 ou 64 bits) 
;-------------------------------- 
Function DetectArchiProc

  ${If} ${RunningX64}
    StrCpy $ArchiProc "64 bit (x64)"
  ${Else}
    StrCpy $ArchiProc "32 bit (x86)"
  ${EndIf}
  
FunctionEnd 

;--------------------------------
; Detection de la presence et de la version de java
;--------------------------------
Function FindJavaVersion  
  
  ; Modification de la vue de la base de registre selon l'architecture processeur
  ${If} ${RunningX64}
    SetRegView 64
  ${EndIf}
  
  StrCpy $1 "SOFTWARE\JavaSoft\Java Runtime Environment"  
  StrCpy $2 0  
  ReadRegStr $2 HKLM "$1" "CurrentVersion"  
  ${If} $2 == ""
    Goto DetectTry2
  ${Else}
    ReadRegStr $5 HKLM "$1\$2" "JavaHome"  
    ${If} $5 == ""
      Goto DetectTry2
    ${Else}
      StrCpy $JavaCurrentVersion $2
      Goto EndFindJavaVersion
    ${EndIf}
  ${EndIf}

  DetectTry2:  
    ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"  
    ${If} $2 == ""
      Goto NoJava
    ${Else}
      ReadRegStr $5 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$2" "JavaHome"  
      ${If} $5 == ""
        Goto NoJava
      ${Else}
        StrCpy $JavaCurrentVersion $2
        Goto EndFindJavaVersion
      ${EndIf}
    ${EndIf}
  
  NoJava:  
    StrCpy $JavaCurrentVersion ""
    
  EndFindJavaVersion:
    
FunctionEnd

;--------------------------------
; Initialisation de la page java
;--------------------------------
Function InitJavaPage

  ; Appel a la fonction de detection d'architecture processeur
  Call DetectArchiProc

  ; Appel a la fonction de detection de java
  Call FindJavaVersion
  
  ; java est absent
  ${If} $JavaCurrentVersion == ""
    StrCpy $JavaTextVersion "Java n'a pas ete detecte sur votre ordinateur."
    StrCpy $JavaTextVersion2 "Ce programme est indispensable au bon fonctionnement de l'application."
  ${Else}
    ; java est present mais faut verifier la version
    ${VersionCompare} $JavaCurrentVersion $JavaRequiredVersion $R0
    ; la version installe est la meme que la requise
    ${If} $R0 == 0
      StrCpy $JavaTextVersion "Votre version de Java est la minimale requise."
      StrCpy $JavaTextVersion2 "Le programme fonctionnera correctement mais nous vous recommandons d'installer la toute derniere version." 
    ${Else}
      ; la version installe est plus recente que celle requise
      ${If} $R0 == 1
        StrCpy $JavaTextVersion "Votre version de Java est superieure a la minimale requise."
        StrCpy $JavaTextVersion2 "Le programme fonctionnera de maniere optimale."
      ${Else}
        StrCpy $JavaTextVersion "Votre version de Java est inferieure a la minimale requise."
        StrCpy $JavaTextVersion2 "Une version recente est indispensable au bon fonctionnement de l'application."
      ${EndIf}
    ${EndIf}
  ${EndIf}
  
FunctionEnd

;--------------------------------
; Page de verification de la presence et de la version de java
;--------------------------------
Function JavaPage
 
  Call InitJavaPage
  
  !insertmacro MUI_HEADER_TEXT "${JavaPageTitle}" "${JavaPageSubTitle}"
	
  nsDialogs::Create 1018
  Pop $JavaDialog

  ${If} $JavaDialog == error
    Abort
  ${EndIf}
	
  ${NSD_CreateLabel} 0 0 100% 12u "Architecture du processeur (32 ou 64 bits) : $ArchiProc"
  Pop $LabelArchiProc

  ${NSD_CreateLabel} 0 13u 100% 12u "Version de Java detecte sur votre ordinateur : $JavaCurrentVersion"
  Pop $LabelJavaCurrentVersion

  ${NSD_CreateLabel} 0 26u 100% 12u "Version de Java minimale requise : $JavaRequiredVersion"
  Pop $LabelJavaRequiredVersion

  ${NSD_CreateLabel} 0 39u 100% 12u "$JavaTextVersion"
  Pop $LabelJavaTextVersion
	
  ${NSD_CreateLabel} 0 52u 100% 24u "$JavaTextVersion2"
  Pop $LabelJavaTextVersion2
  
  ${NSD_CreateLabel} 0 78u 100% 12u "Installation de Java : ${JavaDownloadUrl}"
  Pop $LabelJavaDownloadURL
  
  nsDialogs::Show
  
FunctionEnd
