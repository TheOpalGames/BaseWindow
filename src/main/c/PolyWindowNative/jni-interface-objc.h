#ifndef jni_interface_objc_h
#define jni_interface_objc_h

void callbackFunction(void *cppCtx, char *name);
void charEvent(void *cppCtx, char *name, unsigned short c);
void mouseMove(void *cppCtx, int deltaX, int deltaY);

#endif /* jni_interface_objc_h */
