; ---------------------
;      setup.nsi
;      Written by:  laurent
;      Script de creation de l'installateur et du desinstallateur
; ---------------------

;--------------------------------
; Includes
;--------------------------------

!include "MUI2.nsh"
!include "LogicLib.nsh"
!include "FileFunc.nsh"
!include "nsDialogs.nsh"
!include "x64.nsh"
!include "Sections.nsh"
!include "WordFunc.nsh"
!include "TextFunc.nsh"
!include "Util.nsh"
!include "Java.nsh"
!include "..\..\..\target\project.nsh"

;--------------------------------
; Constantes
;--------------------------------

# Nom de l'application
!define AppName "Reception"
# Fournisseur de l'application
!define Vendor "Laurent"

# Version de java minimale
!define JavaMinVersion "1.6"

# Chemin du programme de desinstallation
!define REG_UNINSTALL "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}"
# Nom du programme de desinstallation
!define UNINSTALLER_NAME "Uninstall.exe"

# Chemin du repertoire temporaire
!define AppTempDir "$TEMP\${AppName}"

;--------------------------------
; Variables
;--------------------------------

Var StartMenuFolder

;--------------------------------
; General
;--------------------------------

; Nom dans le programme d'installation
Name "${AppName} v${PROJECT_VERSION}"

; Chemin d'installation par defaut
InstallDir "C:\${Vendor}\${AppName}"
  
; Recupere le chemin d'installation dans la base de registre si disponible
InstallDirRegKey HKCU "Software\${Vendor}\${AppName}" ""

; Requiert les droits utilisateur
RequestExecutionLevel user
  
;--------------------------------
; Interface Settings
;--------------------------------

; demande de confirmation pour quitter l'installateur
!define MUI_ABORTWARNING
  
;--------------------------------
; Pages de l'installateur
;--------------------------------

; titre de la page de bienvenue sur 3 lignes
!define MUI_WELCOMEPAGE_TITLE_3LINES
; page de bienvenue
!insertmacro MUI_PAGE_WELCOME
  
; page perso de verification de la presence et de la version de java
Page Custom JavaPage
  
; page des composants a installer
!insertmacro MUI_PAGE_COMPONENTS
  
; page du repertoire d'installation
!insertmacro MUI_PAGE_DIRECTORY
  
; Configuration de la page du dossier du menu demarrer
!define MUI_STARTMENUPAGE_DEFAULTFOLDER "${AppName}"
!define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKCU" 
!define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\${Vendor}\${AppName}" 
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "Start Menu Folder"
; page du menu demarrer
!insertmacro MUI_PAGE_STARTMENU Application $StartMenuFolder

; page d'installation des fichiers
!insertmacro MUI_PAGE_INSTFILES
  
; pas de fichier readme
!define MUI_FINISHPAGE_SHOWREADME ""
; decoche la case a cocher readme
!define MUI_FINISHPAGE_SHOWREADME_NOTCHECKED
; texte de la case a cocher readme
!define MUI_FINISHPAGE_SHOWREADME_TEXT "Creer une icone sur le bureau"
; fonction associee a la case a cocher readme
!define MUI_FINISHPAGE_SHOWREADME_FUNCTION CreateDesktopShortcut
; page de fin d'installation
!insertmacro MUI_PAGE_FINISH

;--------------------------------
; Pages du desinstallateur
;--------------------------------

; titre de la page de bienvenue sur 3 lignes
!define MUI_WELCOMEPAGE_TITLE_3LINES
; page de bienvenue du desinstallateur
!insertmacro MUI_UNPAGE_WELCOME
  
; page de confirmation de la desinstallation
!insertmacro MUI_UNPAGE_CONFIRM

; page des composants a desinstaller  
!insertmacro MUI_UNPAGE_COMPONENTS
  
; page de desinstallation
!insertmacro MUI_UNPAGE_INSTFILES
  
; page de fin de desinstallation
!insertmacro MUI_UNPAGE_FINISH
  
;--------------------------------
; Langages
;--------------------------------
 
!insertmacro MUI_LANGUAGE "French"
  
;--------------------------------
; Fonction de creation du raccourci de l'executable sur le bureau
;--------------------------------
Function CreateDesktopShortcut
  CreateShortCut "$DESKTOP\${PROJECT_NAME}.lnk" "$INSTDIR\${PROJECT_NAME}.exe"
FunctionEnd

;--------------------------------
; Section de l'installateur : application
;--------------------------------
Section "Reception" ReceptionSection

  ; definition du chemin cible de l'installateur vers le chemin d'installation de l'application
  SetOutPath "$INSTDIR"
       
  ; ecrasement des fichiers si deja present
  SetOverwrite on
    
  ; copie des fichiers de l'application
  File "${PROJECT_BUILD_DIR}\README.txt"
  File "${PROJECT_BUILD_DIR}\classes\appConfig.properties"
  File "${PROJECT_BUILD_DIR}\classes\log4j.xml"
  File "${PROJECT_BUILD_DIR}\${PROJECT_NAME}.exe"
    
  ; creation de repertoires necessaires a l'application
  CreateDirectory "$INSTDIR\log"
    
  ; ecriture des cles dans la base de registre
  ${GetSize} "$INSTDIR" "/S=0K" $0 $1 $2
  IntFmt $0 "0x%08X" $0
  WriteRegDWORD HKLM "${REG_UNINSTALL}" "EstimatedSize" "$0"
  WriteRegStr HKCU "${REG_UNINSTALL}" "DisplayName" "${AppName}"
  WriteRegStr HKCU "${REG_UNINSTALL}" "DisplayIcon" "$\"$INSTDIR\${PROJECT_NAME}.exe$\""
  WriteRegStr HKCU "${REG_UNINSTALL}" "Publisher" "${Vendor}"
  WriteRegStr HKCU "${REG_UNINSTALL}" "DisplayVersion" "${PROJECT_VERSION}"
  WriteRegStr HKCU "${REG_UNINSTALL}" "InstallLocation" "$\"$INSTDIR$\""
  WriteRegStr HKCU "${REG_UNINSTALL}" "InstallSource" "$\"$EXEDIR$\""
  WriteRegDWord HKCU "${REG_UNINSTALL}" "NoModify" 1
  WriteRegDWord HKCU "${REG_UNINSTALL}" "NoRepair" 1
  WriteRegStr HKCU "${REG_UNINSTALL}" "UninstallString" "$\"$INSTDIR\${UNINSTALLER_NAME}$\""
  WriteRegStr HKCU "${REG_UNINSTALL}" "Comments" "Desinstalle ${PROJECT_NAME}."

  ; sauvegarde des infos specifiques en base de registre
  WriteRegStr HKCU "Software\${Vendor}\${AppName}" "Location" $INSTDIR
  WriteRegStr HKCU "Software\${Vendor}\${AppName}" "Version" ${PROJECT_VERSION}
      
  ; creation du desinstallateur
  WriteUninstaller "$INSTDIR\${UNINSTALLER_NAME}"
  
  ; creation des raccourcis du menu demarrer
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application 
  CreateDirectory "$SMPROGRAMS\$StartMenuFolder"
  CreateShortCut "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk" "$INSTDIR\${UNINSTALLER_NAME}"
  CreateShortCut "$SMPROGRAMS\$StartMenuFolder\${PROJECT_NAME}.lnk" "$INSTDIR\${PROJECT_NAME}.exe"
  CreateShortCut "$SMPROGRAMS\$StartMenuFolder\README.lnk" "$INSTDIR\README.txt"
  !insertmacro MUI_STARTMENU_WRITE_END 

SectionEnd

;--------------------------------
; Section du desinstallateur : application
;--------------------------------
Section "un.Reception" UnReceptionSection

  ; suppression du contenu du repertoire
  Delete "$INSTDIR\${UNINSTALLER_NAME}"
  Delete "$INSTDIR\README.txt"
  Delete "$INSTDIR\appConfig.properties"
  Delete "$INSTDIR\log4j.xml"
  Delete "$INSTDIR\${PROJECT_NAME}.exe"
  
  ; suppression des repertoires utilises par l'application
  RMDir /r "$INSTDIR\log"
    
  ; suppression du repertoire d'installation seulement si vide
  RMDir "$INSTDIR"
  
  ; suppression des raccourcis du menu demarrer
  !insertmacro MUI_STARTMENU_GETFOLDER Application $StartMenuFolder
  Delete "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk"
  Delete "$SMPROGRAMS\$StartMenuFolder\${PROJECT_NAME}.lnk"
  Delete "$SMPROGRAMS\$StartMenuFolder\README.lnk"
  RMDir "$SMPROGRAMS\$StartMenuFolder"
  
  ; suppression du raccourci sur le bureau
  Delete "$DESKTOP\${PROJECT_NAME}.lnk"
  
  ; suppression des cles dans la base de registre
  DeleteRegKey /ifempty HKCU "Software\${Vendor}\${AppName}"
  DeleteRegKey HKCU "${REG_UNINSTALL}"

SectionEnd


;--------------------------------
; Apres la fin de l'initialisation de l'installateur
;--------------------------------
Function .onInit
    
  ; modification de la vue de la base de registre selon qu'il s'agit d'une architecture 32 ou 64 bit
  ${If} ${RunningX64}
    SetRegView 64
  ${EndIf}
  
  ; copie de la version de java requise 
  StrCpy $JavaRequiredVersion ${JavaMinVersion}
        
FunctionEnd

;--------------------------------
; Descriptions
;--------------------------------

  ; chaines de language
  LangString DESC_ReceptionSection ${LANG_FRENCH} "Logiciel d'impression de commandes et encaissements."
  LangString DESC_UnReceptionSection ${LANG_FRENCH} "Logiciel d'impression de commandes et encaissements."
  
  ; assignation des chaines de caracteres aux sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${ReceptionSection} $(DESC_ReceptionSection)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END
  
  !insertmacro MUI_UNFUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${UnReceptionSection} $(DESC_UnReceptionSection)
  !insertmacro MUI_UNFUNCTION_DESCRIPTION_END
  
